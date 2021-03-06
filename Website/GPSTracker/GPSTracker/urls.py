from django.conf.urls import patterns, include, url
from django.conf import settings
#from django.contrib import admin
from django.contrib.gis import admin
from djgeojson.views import GeoJSONLayerView
from tracker.models import ShuttleRoute

admin.autodiscover()

urlpatterns = patterns('',
                       url(r'^$', 'tracker.views.home', name='home'),
                       url(r'^about', 'tracker.views.about', name='about'),
                       url(r'^chat', 'tracker.views.chat', name='chat'),
                       url(r'^chart', 'tracker.views.chart', name='chart'),
                       url(r'^contact', 'tracker.views.contact', name='contact'),
                       url(r'^demo', 'tracker.views.demo', name='demo'),
                       url(r'^features', 'tracker.views.features', name='features'),
                       url(r'^test', 'tracker.views.test', name='test'),
                       url(r'^leaftest', 'tracker.views.leaftest', name='leaftest'),
                       url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
                       url(r'^admin/', include(admin.site.urls)),
                       url(r'^data.geojson$', GeoJSONLayerView.as_view(model=ShuttleRoute), name='data'),
                       )
if settings.DEBUG:
    import debug_toolbar
    urlpatterns += patterns('',
            url(r'^__debug__/',include(debug_toolbar.urls)),
    )
