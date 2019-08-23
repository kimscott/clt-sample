<template>
  <v-container fluid class="pa-0">
    <v-row align="center">
      <v-col cols="12" sm="6">
        <div class="text-center">
          <div class="my-2">
            <v-btn small color="primary" dark>marketOffers Button</v-btn>
          </div>
          <v-text-field label="로그인 이름" v-model="formData.username"></v-text-field>
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
    <v-row align="center">
      <v-col cols="12" sm="6">
        <div>
          <div>
            <v-text-field label="전송데이터" v-model="formData.message"></v-text-field>
          </div>
          <br />
          <div>
            <v-btn small color="primary" dark @click="connect">connect</v-btn>
            <v-btn small color="primary" dark @click="disconnect">disconnect</v-btn>
          </div>
          <div>
            <v-btn small color="error" dark @click="sendWebSocket">/app/message</v-btn>
            <v-btn small color="error" dark @click="sendWebSocket2">/app/message2</v-btn>
          </div>

          <br />
          <div class="text-center">
            <div v-for="item in items2">
              {{ item }}
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
export default {
  name: 'HelloWorld',
  created () {
    var me = this
  },
  data () {
    return {
      evtSource: null,
      msg: 'Welcome to Your Vue.js App',
      stompClient: null,
      items: [],
      items2: [],
      formData: {
        username: 'kook',
        message: 'hello',
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
    connect: function () {
      var me = this
      var socket = new SockJS('http://localhost:8080/websocket')
      me.stompClient = Stomp.over(socket)

      me.stompClient.connect({name: me.formData.username}, function(frame) {
        console.log('Connected: ' + frame)
        console.log(me.stompClient)
        me.stompClient.subscribe('/topic/reply', function(messageOutput) {
          console.log('/topic/reply')
          console.log(messageOutput)
          me.items2.push(messageOutput.body)
        });

        me.stompClient.subscribe('/user/queue/reply', function(messageOutput) {
          console.log('/queue/reply')
          console.log(messageOutput)
          me.items2.push(messageOutput.body)
        });
      });
    },
    disconnect: function () {
      var me = this
      if(me.stompClient != null) {
        me.stompClient.disconnect()
      }

      console.log("Disconnected")
    },
    sendWebSocket: function () {
      var me = this
      me.stompClient.send("/app/message", {}, JSON.stringify({'name': me.formData.message}));
    },
    sendWebSocket2: function () {
      var me = this
      me.stompClient.send("/app/message2", {}, JSON.stringify({'name': me.formData.message}));
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

        me.evtSource.close()
      }
    }
  }
}
</script>
