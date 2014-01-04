from django.contrib import admin
from tracker.models import Location
from tracker.models import Vehicle
from tracker.models import PeopleCount
from tracker.models import StopLocation

admin.site.register(Location)
admin.site.register(Vehicle)
admin.site.register(PeopleCount)
admin.site.register(StopLocation)
