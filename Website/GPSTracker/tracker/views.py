from chartit import DataPool, Chart
from django.shortcuts import render_to_response
from django.http import HttpResponse, HttpResponseRedirect
from django.template import Context, loader, RequestContext
from tracker.models import PeopleCount
def home(request):
    return render_to_response('index.html')

def test(request):
    return render_to_response('test.html')

def chat(request):
    return render_to_response('chat.html')

def chart(request):
    ridershipData = \
            DataPool(
                    series=
                    [{'options': {
                        'source': PeopleCount.objects.all()},
                        'terms': [
                            'Count',
                            'StopID',
                            'LocID']}
                        ])
    riderChart = Chart(
            datasource = ridershipData,
            series_options = 
            [{'options':{
                'type': 'line',
                'stacking': False},
                'terms':{
                    'LocID': [
                        'Count',
                        'StopID']
                    }}],
             chart_options = 
             {'title': {
                 'text': 'Ridership Data'},
                 'xAxis':{
                     'title': {
                         'text': 'Rider Count'}}})

    return render_to_response('chart.html',{'ridershipChart':riderChart})

def help(request):
    return render_to_response('help.html')

def about(request):
    return render_to_response('about.html')

def contact(request):
    return render_to_response('contact.html')
