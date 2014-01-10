from chartit import DataPool, Chart
from django.shortcuts import render_to_response
from tracker.models import PeopleCount
from tracker.models import StopLocation


def home(request):
    return render_to_response('index.html')


def test(request):
    return render_to_response('test.html')


def chat(request):
    return render_to_response('chat.html')


def stopNames(nameID):
    stop = StopLocation.objects.filter(StopID = nameID).values()[0]['StopName'].split(' ')[0]
    return stop


def chart(request):
    ridershipdata = \
        DataPool(
            series=
            [{'options': {
                'source': PeopleCount.objects.all()}, # PeopleCount.objects.all()},
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


def help_page(request):
    return render_to_response('help.html')


def about(request):
    return render_to_response('about.html')


def contact(request):
    return render_to_response('contact.html')
