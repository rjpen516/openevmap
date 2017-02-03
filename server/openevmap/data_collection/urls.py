from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from data_collection import views

urlpatterns = [
    url(r'^evpoint/$', views.evpoint_list),
    url(r'^evpoint/(?P<pk>[0-9]+)/$', views.evpoint_detail),
]

urlpatterns = format_suffix_patterns(urlpatterns)
