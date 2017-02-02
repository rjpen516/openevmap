from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from models import EVPoint
from serializers import EVPointSerializer
# Create your views here.


class JSONResponse(HttpResponse):
    """
    An HttpResponse that renders its content into JSON.
    """
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)


@csrf_exempt
def evpoint_list(request):
    if request.method == 'GET':
        evpoint = EVPoint.objects.all()
        serializer = EVPointSerializer(snippets, many=True)
        return JSONResponse(serializer.data)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = SnippetSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data, status=201)
        return JSONResponse(serializer.errors, status=400)

def evpoint_detail(request, pk):
    """
    Retrieve, update or delete a code snippet.
    """
    try:
        evpoint = EVPoint.objects.get(pk=pk)
    except EVPoint.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = EVPointSerializer(evpoint)
        return JSONResponse(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = EVPointSerializer(evpoint, data=data)
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data)
        return JSONResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        evpoint.delete()
        return HttpResponse(status=204)
