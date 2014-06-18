from django.conf.urls import patterns, include, url
#from django.contrib import admin
from django.contrib.gis import admin

admin.autodiscover()

urlpatterns = patterns('',
                       # Examples:
                       url(r'^$', 'tracker.views.home', name='home'),
                       url(r'^about', 'tracker.views.about', name='about'),
                       url(r'^chat', 'tracker.views.chat', name='chat'),
                       url(r'^chart', 'tracker.views.chart', name='chart'),
                       url(r'^contact', 'tracker.views.contact', name='contact'),
                       url(r'^demo', 'tracker.views.demo', name='demo'),
                       url(r'^features', 'tracker.views.features', name='features'),
                       url(r'^test', 'tracker.views.test', name='test'),
                       url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
                       url(r'^admin/', include(admin.site.urls)),
                       )
