<template>
     <div id="map" style="width: 100%; height: 300px; margin-top:20px"></div>
</template>
<script setup>
import { ref, onMounted, defineEmits, defineProps } from 'vue';
import Map from 'ol/Map.js';
import OSM from 'ol/source/OSM.js';
import TileLayer from 'ol/layer/Tile.js';
import View from 'ol/View.js';
import { fromLonLat } from 'ol/proj';
import { Feature } from 'ol';
import { Point } from 'ol/geom';
import { Vector as VectorLayer } from 'ol/layer';
import { Vector as VectorSource } from 'ol/source';
import { Style, Icon } from 'ol/style';

const props=defineProps({
    location:{
        type:Object,
        default:()=>null
    }
});
const vectorSource = new VectorSource();
const map = ref(null);
onMounted(() => {
    initializeMap();
});
function initializeMap() {
    map.value = new Map({
    target: 'map',
    layers: [
      new TileLayer({
        source: new OSM(),
      }),
      new VectorLayer({
        source: vectorSource
      })
    ],
    view: new View({
      center: fromLonLat([props.location.longitude, props.location.latitude]),
      zoom: 16,
    }),
  });
      const marker = new Feature({
        geometry: new Point(fromLonLat([props.location.longitude, props.location.latitude]))
      });
      marker.setStyle(new Style({
        image: new Icon({
          src: 'https://openlayers.org/en/v4.6.5/examples/data/icon.png',
          anchor: [0.5, 1]
        })
      }));
      vectorSource.addFeature(marker);
}
</script>