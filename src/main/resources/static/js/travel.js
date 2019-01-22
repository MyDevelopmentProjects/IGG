angular.module('HelloApp', [])
    .controller('TravelCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.obj = {};

        $scope.cities = [
            'Schengen'
            , 'Ukraine'
            , 'United Kingdom'
            , 'USA' //2.5
            , 'Canada' //2.5
            , 'Turkey'
            , 'Egypt'
            , 'United Arab Emirates'

            , 'Afghanistan'
            , 'Albania'
            , 'Algeria'
            , 'Andorra'
            , 'Angola'
            , 'Antigua & Deps'
            , 'Argentina'
            , 'Armenia'
            , 'Australia'
            , 'Austria'
            , 'Azerbaijan'
            , 'Bahamas'
            , 'Bahrain'
            , 'Bangladesh'
            , 'Barbados'
            , 'Belarus'
            , 'Belgium'
            , 'Belize'
            , 'Benin'
            , 'Bhutan'
            , 'Bolivia'
            , 'Bosnia and Herzegovina'
            , 'Botswana'
            , 'Brazil'
            , 'Brunei'
            , 'Bulgaria'
            , 'Burkina'
            , 'Burundi'
            , 'Cambodia'
            , 'Cameroon'

            , 'Cape Verde'
            , 'Central African Republic'
            , 'Chad'
            , 'Chile'
            , 'China'
            , 'Colombia'
            , 'Comoros'
            , 'Congo'
            , 'Congo(DemocraticRep)'
            , 'Costa Rica'
            , 'Croatia'
            , 'Cuba'
            , 'Cyprus'
            , 'Czech Republic'
            , 'Denmark'
            , 'Djibouti'
            , 'Dominica'
            , 'Dominican Republic'
            , 'East Timor'
            , 'Ecuador'

            , 'El Salvador'
            , 'Equatorial Guinea'
            , 'Eritrea'
            , 'Estonia'
            , 'Ethiopia'
            , 'Fiji'
            , 'Finland'
            , 'France'
            , 'Gabon'
            , 'Gambia'
            , 'Germany'
            , 'Ghana'
            , 'Greece'
            , 'Grenada'
            , 'Guatemala'
            , 'Guinea'
            , 'Guinea Bissau'
            , 'Guyana'
            , 'Haiti'
            , 'Honduras'
            , 'Hungary'
            , 'Iceland'
            , 'India'
            , 'Indonesia'
            , 'Iran'
            , 'Iraq'
            , 'Ireland'
            , 'Israel'
            , 'Italy'
            , 'IvoryCoast'
            , 'Jamaica'
            , 'Japan'
            , 'Jordan'
            , 'Kazakhstan'
            , 'Kenya'
            , 'Kiribati'
            , 'KoreaNorth'
            , 'KoreaSouth'
            , 'Kosovo'
            , 'Kuwait'
            , 'Kyrgyzstan'
            , 'Laos'
            , 'Latvia'
            , 'Lebanon'
            , 'Lesotho'
            , 'Liberia'
            , 'Libya'
            , 'Liechtenstein'
            , 'Lithuania'
            , 'Luxembourg'
            , 'Macedonia'
            , 'Madagascar'
            , 'Malawi'
            , 'Malaysia'
            , 'Maldives'
            , 'Mali'
            , 'Malta'
            , 'Marshall Islands'
            , 'Mauritania'
            , 'Mauritius'
            , 'Mexico'
            , 'Micronesia'
            , 'Moldova'
            , 'Monaco'
            , 'Mongolia'
            , 'Montenegro'
            , 'Morocco'
            , 'Mozambique'
            , 'Myanmar,(Burma)'
            , 'Namibia'
            , 'Nauru'
            , 'Nepal'
            , 'Netherlands'
            , 'New Zealand'
            , 'Nicaragua'
            , 'Niger'
            , 'Nigeria'
            , 'Norway'
            , 'Oman'
            , 'Pakistan'
            , 'Palau'
            , 'Panama'
            , 'Papua New Guinea'
            , 'Paraguay'
            , 'Peru'
            , 'Philippines'
            , 'Poland'
            , 'Portugal'
            , 'Qatar'
            , 'Romania'
            , 'Russia Federation'
            , 'Rwanda'
            , 'StKitts&;Nevis'
            , 'Saint Lucia'
            , 'Vincent&;theGrenadines'
            , 'Samoa'
            , 'San Marino'
            , 'SaoTome&;Principe'
            , 'Saudi Arabia'
            , 'Senegal'
            , 'Serbia'
            , 'Seychelles'
            , 'Sierra Leone'
            , 'Singapore'
            , 'Slovakia'
            , 'Slovenia'
            , 'Solomon Islands'
            , 'Somalia'
            , 'South Africa'
            , 'Sudan'
            , 'Spain'
            , 'Sri Lanka'
            , 'Sudan'
            , 'Suriname'
            , 'Swaziland'
            , 'Sweden'
            , 'Switzerland'
            , 'Syria'
            , 'Taiwan'
            , 'Tajikistan'
            , 'Tanzania'
            , 'Thailand'
            , 'Togo'
            , 'Tonga'
            , 'Trinidad&;Tobago'
            , 'Tunisia'

            , 'Turkmenistan'
            , 'Tuvalu'
            , 'Uganda'


            , 'Uruguay'
            , 'Uzbekistan'
            , 'Vanuatu'
            , 'Vatican City'
            , 'Venezuela'
            , 'Vietnam'
            , 'Yemen'
            , 'Zambia'
            , 'Zimbabwe'];

        $scope.selectedCities = [];

        $scope.addCountry = function (item) {
            if (item && item !== "") {
                $scope.selectedCities.push(item)
                $scope.selectedCity = '';
            }
        };

        $scope.calculate = function () {

            if (!$scope.obj.dob || $scope.selectedCities.length === 0 || !$scope.obj.startdate || !$scope.obj.enddate) {
                return 0
            }

            var dob = $scope.calculateAge($scope.parseDate($scope.obj.dob));

            var startPrice = 1;

            for (var i = 0; i < $scope.selectedCities.length; i++) {
                var city = $scope.selectedCities[i];
                if (city === 'USA' || city === 'Canada') {
                    startPrice = 2;
                    break;
                }
            }

            var numberOfDays = $scope.calculateDay($scope.parseDate($scope.obj.startdate), $scope.parseDate($scope.obj.enddate));
            if (numberOfDays > 0) {
                startPrice *= numberOfDays;
            }

            if (dob < 15) {
                startPrice /= 2
            } else if (dob >= 65 && dob <= 75) {
                startPrice *= 2
            } else if (dob > 75) {
                return 0
            }

            if ($scope.obj.bagage) {
                startPrice += 10
            }

            return startPrice;

        };

        $scope.removeCity = function(item) {
            for (var i = 0; i < $scope.selectedCities.length; i++) {
                if (item === $scope.selectedCities[i]) {
                    $scope.selectedCities.splice(i, 1);
                    break
                }
            }
        };

        $scope.parseDate = function (str) {
            var mdy = str.split('/');
            return new Date(mdy[2], mdy[1] - 1, mdy[0]);
        };

        $scope.calculateDay = function (start, end) {
            return Math.abs(Math.round((start - end) / (1000 * 60 * 60 * 24))) + 1;
        };

        $scope.calculateAge = function (dt) {
            var ageDifMs = Date.now() - dt.getTime();
            var ageDate = new Date(ageDifMs);
            return Math.abs(ageDate.getUTCFullYear() - 1970);
        };

        $scope.add = function () {

            $('.btn').attr('disabled', 'disabled')

            for (var i = 0; i < $scope.selectedCities.length; i++) {
                if ($scope.selectedCities[i] !== '' && $scope.selectedCities[i] !== undefined)
                    $scope.obj.countrylist += $scope.selectedCities[i];
            }

            if ($scope.obj.bagage) {
                $scope.obj.bagage = "true";
            }

            $http.post('travel/addPolicy', $scope.obj).success(function (resp) {
                if (resp.transId && resp.transId !== "") {
                    window.location = 'http://178.128.198.46:8090/post.php?trans_id=' + encodeURIComponent(resp.transId)
                } else {
                    alert("შეცდომა დაფიქსირდა ოპერაციის შესრულებისას. სცადეთ თავიდან/Error while performing request. Try again later")
                    $('.btn').removeAttribute('disabled')
                }
            });
        }

        $(function () {
            $("#datepicker, #datepicker2, #datepicker3, #datepicker4").datepicker({
                autoclose: true,
                todayHighlight: true,
            }).datepicker('update', new Date());
        });

    }]);
