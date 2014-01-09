from django.contrib import admin
from tracker.models import Location
from tracker.models import Vehicle
from tracker.models import PeopleCount
from tracker.models import StopLocation

admin.site.register(Location)
admin.site.register(Vehicle)
admin.site.register(StopLocation)
class PeopleCountAdmin(admin.ModelAdmin):
    list_display = ('CountID', 'StopID', 'VehID', 'LocID', 'Date', 'Time', \
            'Count',)
    search_fields = ('StopID', 'Date', 'Time', 'Count')
    list_filter = ('Time',)

    readonly_fields = ('CountID', 'Date','Time', 'peoplecountchart')
    fieldsets = [
            ('PeopleCount', { 'fields': [ 'CountID', 'StopID', 'VehID', 'LocID', 'Date', 'Time', \
            'Count',]}), \
            ('PeopleCount Chart', { 'fields': ['peoplecountchart', ]}),]

admin.site.register(PeopleCount, PeopleCountAdmin)
