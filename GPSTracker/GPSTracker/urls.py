from django.conf.urls import patterns, include, url
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
# Examples:
            url(r'^$', 'tracker.views.home', name='home'),
            url(r'^about', 'tracker.views.about', name='about'),
            url(r'^contact', 'tracker.views.contact', name='contact'),
            url(r'^help', 'tracker.views.help', name='help'),
			url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
			url(r'^admin/', include(admin.site.urls)),
			)
