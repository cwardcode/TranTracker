from django.db import models

class Location(models.Model):
    LocID = models.AutoField(primary_key=True)
    VehicleID = models.IntegerField(max_length=3)
    Latitude = models.DecimalField(max_digits=10, decimal_places=6)
    Longitude = models.DecimalField(max_digits=10, decimal_places=6)
    Speed = models.DecimalField(max_digits=4, decimal_places=1)
    Title = models.CharField(max_length=40)
    
    def __unicode__(self):
        return "Location ID:" + str(self.LocID)

