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
# Create your views here.


class EVPointList(APIView):
    def get(self, request, format=None):
        points = EVPoint.objects.all()
        serilizerd = EVPointSerializer(points,many=True)
        return Response(serilizer.data)
    def post(self, request, format=None):
        serilizer = EVPoint.objects.all()
        if serilizer.is_valid():
            serilizer.save()
            return Response(serilizer.data, status=status.HTTP_201_CREATED)
        return Response(serilizer.errors, status=status.HTTP_400_BAD_REQUEST)

class EVPointDetail(APIView):
    def get_object(self,pk):
        try:
            return EVPoint.objects.get(pk=pk)
        except EVPoint.DoesNotExist:
            raise Http404

    def get(self,request,pk, format=None):
        point = self.get_object(pk)
        serilizer = EVPointSerializer(point)
        return Response(serilizer.data)

    def put(self,request, pk, format=None):
        point = self.get_object(pk)
        serilizer = EVPointSerializer(point, data=request.data)
        if serilizer.is_valid():
            serilizer.save()
            return Response(serilizer.data)
        return Response(serilizer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self,request, pk, format=None):
        point = self.get_object(pk)
        point.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
