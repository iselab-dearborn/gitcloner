class Tasks {
    
    constructor(){
        
        const that = this;
        
        this.eventEmitter = new EventEmitter();
        
        this.stompClient = new StompJs.Client({
            reconnectDelay: 1000,
            debug: function (str) {
                //console.log(str);
            },
        });
        
        this.stompClient.webSocketFactory = function() {     
            return new SockJS('/ws', [], {});
        };
        
        this.stompClient.onConnect = function(frame) {
            that.onConnect(frame);
        };
        
        this.stompClient.onStompError = function (frame) {
            console.log('Broker reported error: ' + frame.headers['message']);
            console.log('Additional details: ' + frame.body);
        };
        
        this.stompClient.onWebSocketClose = function (frame) {
        
            if (frame.reason) {
                console.error(frame.reason+". Reconnecting...");
            }
        };
        
        this.stompClient.activate();
    }  
    
    on(event, cb){
        this.eventEmitter.on(event, cb);
    }
    
    onConnect(frame){
        
        console.log("Connected to Websocket");
        
        const that = this;
        
        this.stompClient.subscribe(`/topic/running-tasks/create`, function (payload) { 
            that.eventEmitter.emit("create", payload);
        });
        
        this.stompClient.subscribe(`/topic/running-tasks/update`, function (payload) { 
            that.eventEmitter.emit("update", payload);
        });
        
        this.stompClient.subscribe(`/topic/running-tasks/done`, function (payload) { 
            that.eventEmitter.emit("done", payload);
        });
        
        this.stompClient.subscribe(`/topic/running-tasks/error`, function (payload) { 
            that.eventEmitter.emit("error", payload);
        });
        
        this.stompClient.publish({destination: `/app/running-tasks/join`});
    }
}

