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
    estWait = models.DecimalField(max_digits=8, decimal_places=5)
    NextStop = models.CharField(max_length=25)

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

    def totalPeople(self):
        totPeople = 0
        for model in PeopleCount.objects.all():
            totPeople += model.Count
        return totPeople

    def totalPeopleByStop(self, stopID):
        totPeople = 0
        for model in PeopleCount.objects.filter(StopID=stopID):
            totPeople += model.Count
        return totPeople

    def getStopNames(self):
        nameArray = [];
        for model in Location.objects.all():
            nameArray.append(str(model.LocID))
        return nameArray

    def __unicode__(self):
        return str(self.CountID)

    def peoplecount_chart(self):
       stopNames = self.getStopNames()
       totPeople = self.totalPeople()
       stopIDStr = str(self.StopID) 
       if stopIDStr == "Kimmell":
           totAtStop = self.totalPeopleByStop(20)
       elif stopIDStr == "Coulter":
               totAtStop = self.totalPeopleByStop(19)
       elif stopIDStr == "OneStop":
               totAtStop = self.totalPeopleByStop(18)
       elif stopIDStr == "Central":
               totAtStop = self.totalPeopleByStop(17)
       elif stopIDStr == "Harrill":
               totAtStop = self.totalPeopleByStop(16)
       elif stopIDStr == "AlbrightBenton":
               totAtStop = self.totalPeopleByStop(15)
       elif stopIDStr == "Reynolds":
               totAtStop = self.totalPeopleByStop(14)
       elif stopIDStr == "Moore":
               totAtStop = self.totalPeopleByStop(13)
       elif stopIDStr == "Hunter Library":
               totAtStop = self.totalPeopleByStop(12)
       elif stopIDStr == "UC":
               totAtStop = self.totalPeopleByStop(11)
       elif stopIDStr == "Norton":
               totAtStop = self.totalPeopleByStop(10)
       elif stopIDStr == "The Village":
               totAtStop = self.totalPeopleByStop(9)
       elif stopIDStr == "Walker":
               totAtStop = self.totalPeopleByStop(8)
       elif stopIDStr == "Food Court":
               totAtStop = self.totalPeopleByStop(7)
       elif stopIDStr == "Bardo Arts Center":
               totAtStop = self.totalPeopleByStop(6)
       elif stopIDStr == "Field House":
               totAtStop = self.totalPeopleByStop(5)
       elif stopIDStr == "South Baseball Lot":
               totAtStop = self.totalPeopleByStop(4)
       elif stopIDStr == "North Baseball Lot":
               totAtStop = self.totalPeopleByStop(3)
       elif stopIDStr == "Ramsey":
               totAtStop = self.totalPeopleByStop(2)
       else:
           if stopIDStr == "McKee":
               totAtStop = self.totalPeopleByStop(1)

       lu = { 'categories' : [self.StopID],\
               'names' : [stopNames],\
               'tot_riders' : [self.Count],\
               'tot_riders_at_stop' : [totAtStop]} 

       return render_to_string('admin/tracker/peoplecount/peoplecount_chart.html', lu )
    peoplecount_chart.allow_tags = True

    def allstops_chart(self):

       totPeople = self.totalPeople()
       totPeopleAtRamsey = self.totalPeopleByStop(2);
       lu = { 'categories' : [self.StopID],\
               'tot_riders' : [self.Count],\
               'tot_riders_at_stop' : [totPeople]} 

       return render_to_string('admin/tracker/peoplecount/allstops_chart.html', lu )
    allstops_chart.allow_tags = True


class StopLocation(models.Model):
    StopID = models.AutoField(primary_key=True)
    StopName = models.CharField(max_length=40)
    Latitude = models.DecimalField(max_digits=10, decimal_places=6)
    Longitude = models.DecimalField(max_digits=10, decimal_places=6)

    def __unicode__(self):
        #VehID + LocID Identifier
        return str(self.StopName)


