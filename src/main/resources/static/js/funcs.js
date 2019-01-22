var httpReqManager = {
    baseUrl: "http://localhost:8080/api/",
}

angular.module('HelloApp', [])
    .controller('HelloCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.products = [];
        $scope.object = {};

        $http.get(httpReqManager.baseUrl + "listAllProducts").success(function (response) {
            $scope.products = response;
        });
    }]);

function resize() {
    var height = document.getElementsByTagName("html")[0].scrollHeight;
    window.parent.postMessage(["setHeight", height], "*");
}