app.service('configService',function ($http) {
    this.getWebSocketUrl = function () {
        return $http.get('../config/getWebSocketUrl');
    };
});