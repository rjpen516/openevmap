from __future__ import unicode_literals

from django.db import models
import settings

# Create your models here.
class EVPoint(models.Model):
    longitude = models.DecimalField(max_digits=9, decimal_places=6)
    latitude = models.DecimalField(max_digits=9, decimal_places=6)
    tempature =  models.IntegerField()
    speed = models.DecimalField(max_digits=5,decimal_places=2)
    energy_usage = models.DecimalField(max_digits=10,decimal_places=3)
    user = models.OneToOneField(settings.AUTH_USER_MODEL)

    def __str__(self):
        return ("%d:%d @ %d"%(self.longitude,self.latitude, self.speed))
