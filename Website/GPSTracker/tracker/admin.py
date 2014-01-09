from django.contrib import admin
from tracker.models import Location
from tracker.models import Vehicle
from tracker.models import PeopleCount
from tracker.models import StopLocation


class LocationAdmin(admin.ModelAdmin):
    list_display = ('LocID', 'VehID', 'Latitude', 'Longitude', 'Speed',)
    search_fields = ('VehID', 'LocID', 'Speed')
    list_filter = ('VehID',)

    readonly_fields = ('LocID', 'VehID', 'Latitude', 'Longitude', 'Speed')
    fieldsets = [
        ('Location', {'fields': ['LocID', 'VehID', 'Latitude', 'Longitude', 'Speed', ]}), ]

admin.site.register(Location, LocationAdmin)


class VehicleAdmin(admin.ModelAdmin):
    list_display = ('VehID', 'Title', 'Driver',)
    search_fields = ('Title', 'Driver')
    list_filter = ('Title', 'Driver',)

    readonly_fields = ('VehID', 'Title')
    fieldsets = [
        ('Vehicle', {'fields': ['VehID', 'Title', 'Driver']}), ]

admin.site.register(Vehicle, VehicleAdmin)


class StopLocationAdmin(admin.ModelAdmin):
    list_display = ('StopID', 'StopName', 'Latitude', 'Longitude',)
    search_fields = ('StopName',)
    list_filter = ('StopName',)

    readonly_fields = ('StopID', 'StopName', 'Latitude', 'Longitude')
    fieldsets = [
        ('StopLocation', {'fields': ['StopID', 'StopName', 'Latitude', 'Longitude']}), ]

admin.site.register(StopLocation, StopLocationAdmin)


class PeopleCountAdmin(admin.ModelAdmin):
    list_display = ('CountID', 'StopID', 'VehID', 'LocID', 'Date', 'Time',
                    'Count',)
    search_fields = ('StopID', 'Date', 'Time', 'Count')
    list_filter = ('Time',)

    readonly_fields = ('CountID', 'Date', 'Time', 'peoplecountchart')
    fieldsets = [
        ('PeopleCount', {'fields': ['CountID', 'StopID', 'VehID', 'LocID', 'Date', 'Time',
                                    'Count', ]}),
        ('PeopleCount Chart', {'fields': ['peoplecountchart', ]}), ]

admin.site.register(PeopleCount, PeopleCountAdmin)

