from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
import views

urlpatterns = [
    url(r'^$', views.evpoint_list),
    url(r'^(?P<pk>[0-9]+)/$', views.evpoint_detail),
]

urlpatterns = format_suffix_patterns(urlpatterns)
