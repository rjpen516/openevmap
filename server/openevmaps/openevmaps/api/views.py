from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from rest_framework.views import APIView
from models import EVPoint
from serializers import EVPointSerializer, UserSerializer, UserSerializerList
from rest_framework import status
from rest_framework.decorators import api_view, permission_classes
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework import mixins
from rest_framework import generics
from django.conf import settings
from django.contrib.auth.models import User
from django.contrib.auth import get_user_model
from rest_framework import permissions
from permissions import IsOwnerOrReadOnly, IsOwner
from rest_framework.reverse import reverse


# Create your views here.


@api_view(['GET'])
def api_root(request, format=None):
    return Response({
        'users': reverse('user-list', request=request, format=format),
        'evpoints': reverse('evpoints-list', request=request, format=format)
    })

class EVPointList(generics.ListCreateAPIView):
    queryset = EVPoint.objects.all()
    serializer_class = EVPointSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly,)
    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)

class EVPointDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = EVPoint.objects.all()
    serializer_class = EVPointSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly,
                      IsOwnerOrReadOnly,)


class UserList(generics.ListAPIView):
    queryset = get_user_model().objects.all()
    serializer_class = UserSerializerList
    permission_classes = (permissions.IsAuthenticated,)


class UserDetail(generics.RetrieveAPIView):
    queryset = get_user_model().objects.all()
    serializer_class = UserSerializer
    permission_classes = (IsOwner,)
