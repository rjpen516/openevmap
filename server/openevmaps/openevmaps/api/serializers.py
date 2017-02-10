from rest_framework import serializers
from .models import EVPoint
from django.conf import settings
from django.contrib.auth.models import User
from django.contrib.auth import get_user_model



class EVPointSerializer(serializers.ModelSerializer):
    owner = serializers.ReadOnlyField(source='owner.username')
    class Meta:
        model = EVPoint
        fields = ('id', 'longitude', 'latitude', 'tempature', 'speed', 'energy_usage','owner')


class UserSerializerList(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ('id',)

class UserSerializer(serializers.ModelSerializer):
    #evpoints = serializers.PrimaryKeyRelatedField(many=True, queryset=EVPoint.objects.all())

    class Meta:
        model = get_user_model()
        fields = ('id', 'username', 'email')
