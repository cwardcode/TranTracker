from chartit import DataPool, Chart
from django.shortcuts import render_to_response
from tracker.models import PeopleCount


def home(request):
    return render_to_response('index.html')


def test(request):
    return render_to_response('test.html')


def chat(request):
    return render_to_response('chat.html')


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
              'LocID': [
                  'Count']
          }}],
        chart_options=
        {'title': {
            'text': 'Ridership Data'},
         'xAxis': {
             'title': {
                 'text': 'Rider Count'}}})

    return render_to_response('chart.html', {'ridershipChart': riderchart})


def help_page(request):
    return render_to_response('help.html')


def about(request):
    return render_to_response('about.html')


def contact(request):
    return render_to_response('contact.html')
