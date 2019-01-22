angular.module('HelloApp', [])
    .controller('DocSign', ['$scope', '$http', function ($scope, $http) {

        $scope.isLoading = false;

        $scope.add = function() {

            var data = new FormData();

            $.each($("input[type='file']"), function (j, obj) {
                $.each($(obj)[0].files, function(i, file) {
                    data.append('file', file);
                });
            })

            var urlParams = '&fullname=' + $scope.object.fullName;
            urlParams += '&phone=' + $scope.object.mobile;
            urlParams += '&email=' + $scope.object.email;

            $scope.isLoading = true;

            $.ajax({
                type: 'POST',
                url: '/doc/upload?type=USER_IMG' + urlParams,
                cache: false,
                contentType: false,
                processData: false,
                data : data,
                success: function(result){
                    if(!result.content || !result.success) {
                        alert("შეცდომა ოპერაციის შესრულებისას");
                    } else {
                        alert("ოპერაცია შესრულდა წარმატებით");
                    }
                    window.location.reload()
                },
                error: function(err){
                    window.location.reload()
                }
            })

        }

        $(function () {
            $("#datepicker").datepicker({
                autoclose: true,
                todayHighlight: true
            }).datepicker('update', new Date());
        });
    }]);