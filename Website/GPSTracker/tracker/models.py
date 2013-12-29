from django.db import models

class Vehicle(models.Model):
    VehID = models.AutoField(primary_key=True)
    Title = models.CharField(max_length=40)

    def __unicode__(self):
        return self.Title

class Location(models.Model):
    LocID = models.AutoField(primary_key=True)
    VehID = models.ForeignKey('Vehicle')
    Latitude = models.DecimalField(max_digits=10, decimal_places=6)
    Longitude = models.DecimalField(max_digits=10, decimal_places=6)
    Speed = models.DecimalField(max_digits=4, decimal_places=1)
    
    def __unicode__(self):
        #VehID + LocID Identifier
        return str(self.VehID) + "/"+ str(self.LocID)

