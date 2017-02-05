from django.conf.urls import url, include
from rest_framework.urlpatterns import format_suffix_patterns
from rest_framework_jwt.views import obtain_jwt_token
import views

urlpatterns = format_suffix_patterns([
    url(r'^$', views.api_root),
    url(r'^evpoints/$', views.EVPointList.as_view(), name='evpoints-list'),
    url(r'^evpoints/(?P<pk>[0-9]+)/$', views.EVPointDetail.as_view(), name='evpoints-detail'),
    url(r'^users/$', views.UserList.as_view(), name='user-list'),
    url(r'^users/(?P<pk>[0-9]+)/$', views.UserDetail.as_view(), name='user-detail'),
])
urlpatterns += [
    url(r'^api-auth/', include('rest_framework.urls',
                               namespace='rest_framework')),
    url(r'^rest-auth/', include('rest_auth.urls')),
    url(r'^api-token-auth/', obtain_jwt_token),
]
