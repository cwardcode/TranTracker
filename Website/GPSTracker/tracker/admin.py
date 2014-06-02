from django.contrib import admin
from django.shortcuts import render_to_response
from chartit import DataPool, Chart 
from tracker.models import Location
from tracker.models import Vehicle
from tracker.models import PeopleCount
from tracker.models import StopLocation


class LocationAdmin(admin.ModelAdmin):
    list_display = ('LocID', 'VehID', 'Latitude', 'Longitude', 'Speed', 
                                                         'estWait', 'NextStop',)
    search_fields = ('VehID', 'LocID', 'Speed')
    list_filter = ('VehID',)

    readonly_fields = ('LocID', 'VehID', 'Latitude', 'Longitude', 'Speed')
    fieldsets = [
        ('Location', {'fields': ['LocID', 'VehID', 'Latitude', 'Longitude', 
                                            'Speed','estWait','NextStop', ]}), ]

admin.site.register(Location, LocationAdmin)


class VehicleAdmin(admin.ModelAdmin):
    list_display = ('VehID', 'Title', 'Driver',)
    search_fields = ('Title', 'Driver')
    list_filter = ('Title', 'Driver',)

    readonly_fields = ('VehID',)
    fieldsets = [
        ('Vehicle', {'fields': ['VehID', 'Title', 'Driver']}), ]

admin.site.register(Vehicle, VehicleAdmin)


class StopLocationAdmin(admin.ModelAdmin):
    list_display = ('StopID', 'StopName', 'Latitude', 'Longitude',)
    search_fields = ('StopName',)
    list_filter = ('StopName',)

    readonly_fields = ('StopID',)
    fieldsets = [
        ('StopLocation', {'fields': ['StopID', 'StopName', 'Latitude', 'Longitude']}), ]

admin.site.register(StopLocation, StopLocationAdmin)

class ChartChangeList(admin.ModelAdmin):
    list_display = ('StopID', 'VehID','Date', 'Time', 'Count',)
    list_filter = ('StopID',)

    def changelist_view(self, request, extra_context=None):
        ridershipdata = \
            DataPool(
                series=
                [{'options': {
                    'source': PeopleCount.objects.all()},
                  'terms': [
                      'Count',
                      'StopID',
                      'LocID']}
                ])
        riderchart = Chart(
            datasource=ridershipdata,
            series_options=
            [{'options': {
                'type': 'pie',
                'stacking': False},
              'terms': {
                  'StopID': [
                      'Count']
              }}],
            chart_options=
            {'title': {
                'text': 'Ridership Data'},
             'xAxis': {
                 'title': {
                     'text': 'Rider Count'}}},
                 x_sortf_mapf_mts=(None, stopNames, False))

        return render_to_response('chart.html', {'ridershipChart': riderchart})

class PeopleCountAdmin(admin.ModelAdmin):
    list_display = ('CountID', 'StopID', 'VehID', 'LocID', 'Date', 'Time',
                    'Count',)
    search_fields = ('StopID', 'Date', 'Time', 'Count')
    list_filter = ('Time',)
    #Re-enable this when we can automatically post ridership counts from app
    #readonly_fields = ('CountID', 'StopID', 'VehID', 'LocID','Count','Date', 'Time', 'peoplecount_chart')
    readonly_fields = ('CountID', 'Date', 'Time', 'peoplecount_chart')
    fieldsets = [
        ('PeopleCount', {'fields': ['CountID', 'StopID', 'VehID', 'LocID', 'Date', 'Time',
                                    'Count', ]}),
        ('PeopleCount Chart', {'fields': ['peoplecount_chart', ]}), ]

admin.site.register(PeopleCount, PeopleCountAdmin)

