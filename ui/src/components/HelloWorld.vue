<template>
  <v-container fluid class="pa-0">
    <v-row align="center">
      <v-col cols="12" sm="6">
        <div class="text-center">
          <div class="my-2">
            <v-btn small color="primary" dark>marketOffers Button</v-btn>
          </div>
          <v-text-field label="이름" v-model="formData.username"></v-text-field>
          <v-text-field label="거래아이디" v-model="formData.offerId"></v-text-field>
          <div class="my-2">
            <v-btn small color="primary" dark @click="slotOffer">slotOffer  Button</v-btn>
          </div>
        </div>
        <div class="text-center">
          <div v-for="item in items">
            {{ item }}
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'HelloWorld',
  created () {
    var me = this
  },
  data () {
    return {
      evtSource: null,
      msg: 'Welcome to Your Vue.js App',
      items: [],
      formData: {
        username: 'kook',
        offerId: 1
      }
    }
  },
  methods: {
    slotOffer: function () {
      var me = this
      me.startSSE()
      me.$http.get(`${window.API_HOST}/product/slotOffer/${me.formData.offerId}?username=${me.formData.username}`, {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      ).then(function (e) {

      })
    },
    startSSE: function () {
      console.log('start SSE')
      var me = this
      console.log(me.evtSource)
      if (me.evtSource != null) {
        me.evtSource.close()
      }

      me.evtSource = new EventSource(`${window.API_HOST}/market/sse?username=${me.formData.username}&offerId=${me.formData.offerId}`)

      me.evtSource.onmessage = function (e) {
        var parseMessage = JSON.parse(e.data)
        var tmpData = JSON.parse(parseMessage.message)
        console.log(tmpData)
        me.items.push(tmpData)

        console.log(me.items)

        me.evtSource.close();
      }
    }
  }
}
</script>
