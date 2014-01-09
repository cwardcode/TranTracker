from django.db import models
from django.template.loader import render_to_string

class Vehicle(models.Model):
    VehID = models.AutoField(primary_key=True)
    Title = models.CharField(max_length=40)
    Driver = models.CharField(max_length=25)

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
        return str(self.LocID)


class PeopleCount(models.Model):
    CountID = models.AutoField(primary_key=True)
    StopID = models.ForeignKey('StopLocation')
    VehID = models.ForeignKey('Vehicle')
    LocID = models.ForeignKey('Location')
    Date = models.DateField(auto_now_add=True, blank=False)
    Time = models.TimeField(auto_now_add=True)
    Count = models.IntegerField()

    Date.editable = True
    Time.editable = True

    def __unicode__(self):
        return str(self.CountID)

    def peoplecountchart(self):
        lu = { 'categories' : self.StopID,\
             'count' : self.Count,}
        lu['total_riders'] = self.Count]

        return render_to_string('admin/tracker/peoplecount_chart.html', lu )
    peoplecountchart.allow_tags = True

class StopLocation(models.Model):
    StopID = models.AutoField(primary_key=True)
    StopName = models.CharField(max_length=40)
    Latitude = models.DecimalField(max_digits=10, decimal_places=6)
    Longitude = models.DecimalField(max_digits=10, decimal_places=6)

    def __unicode__(self):
        #VehID + LocID Identifier
        return str(self.StopName)


