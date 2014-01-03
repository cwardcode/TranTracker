from django.shortcuts import render_to_response
from django.http import HttpResponse, HttpResponseRedirect
from django.template import Context, loader, RequestContext

def home(request):
    return render_to_response('index.html')

def test(request):
    return render_to_response('test.html')

def chat(request):
    return render_to_response('chat.html')

def help(request):
    return render_to_response('help.html')

def about(request):
    return render_to_response('about.html')

def contact(request):
    return render_to_response('contact.html')
