from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
import views

urlpatterns = [
    url(r'^$', views.EVPointList.as_view()),
    url(r'^(?P<pk>[0-9]+)/$', views.EVPointDetail.as_view()),
]

urlpatterns = format_suffix_patterns(urlpatterns)
