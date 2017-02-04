from rest_framework import serializers
from models import EVPoint



class EVPointSerializer(serializers.ModelSerializer):
    class Meta:
        model = EVPoint
        fields = ('id', 'longitude', 'latitude', 'tempature', 'speed', 'energy_usage')

#class EVPointSerializer(serializers.Serializer):
#     id = serializers.IntegerField(read_only=True)
#     longitude = serializers.DecimalField(max_digits=9, decimal_places=6)
#     latitude = serializers.DecimalField(max_digits=9, decimal_places=6)
#     tempature = serializers.IntegerField()
#     speed = serializers.DecimalField(max_digits=5, decimal_places=2)
#     energy_usage = serializers.DecimalField(max_digits=10,decimal_places=3)
#
#     def create(self, validated_data):
#         return Snippet.objects.create(**validated_data)
#
#    def update(self,instance,validated_data):
#        instance.longitude = validated_data.get('longitude',instance.longitude)
##        instance.tempature = validated_data('tempature', instance.tempature)
#        instance.speed = validated_data('speed', instance.speed)
#        instance.energy_usage = validated_data('energy_usage', instance.energy_usage)
#        instance.save()
#        return instance
