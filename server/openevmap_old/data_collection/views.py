from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from models import EVPoint
from serializers import EVPointSerializer
from rest_framework import status
from rest_framework.decorators import api_view, permission_classes
from rest_framework import permissions
from rest_framework.response import Response
# Create your views here.


class JSONResponse(HttpResponse):
    """
    An HttpResponse that renders its content into JSON.
    """
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)


@api_view(['GET','POST'])
@permission_classes((permissions.AllowAny,))
def evpoint_list(request, format=None):
    if request.method == 'GET':
        evpoint = EVPoint.objects.all()
        serializer = EVPointSerializer(evpoint, many=True)
        return Response(serializer.data)
    elif request.method == 'POST':
        serializer = EVPointSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET', 'PUT', 'DELETE'])
@permission_classes((permissions.AllowAny,))
def evpoint_detail(request, pk, format=None):
    """
    Retrieve, update or delete a snippet instance.
    """
    try:
        evpoint = EVPoint.objects.get(pk=pk)
    except EVPoint.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = EVPointSerializer(evpoint)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = EVPointSerializer(evpoint, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        evpoint.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
