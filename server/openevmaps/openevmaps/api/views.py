from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from rest_framework.views import APIView
from models import EVPoint
from serializers import EVPointSerializer
from rest_framework import status
from rest_framework.decorators import api_view, permission_classes
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework import mixins
from rest_framework import generics
# Create your views here.


class EVPointList(generics.ListCreateAPIView):
    queryset = EVPoint.objects.all()
    serializer_class = EVPointSerializer

class EVPointDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = EVPoint.objects.all()
    serializer_class = EVPointSerializer
