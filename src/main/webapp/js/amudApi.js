window.amudApi = (function () {

	var buildURL = function (txt) {
		if(-1 === txt.indexOf(",")) {
			return "/GetLocationsByText?txt=" + txt;
		} else {
			var coords = txt.split(',');
			return "/GetLocationsByPos?lat=" + coords[0] + "&lon=" + coords[1];
		}
	};
	
    var getLocationsByText = function (txt, callback) {
        if (txt.length == 0)
            callback([]);
        else {
            var url = buildURL(txt);
            $.ajax({
                url: url,
                dataType: "json",
                success: function (data) { 
                	var ret = $.grep(data.results || [], function (location) {
                		return location.name; 
                	});
                	callback(ret); 
                },
            	error: function (jqXHR, textStatus, errorThrown) {
            		console.log(textStatus + " " + errorThrown);
            	} 
            });
        }
    };

    return {
    	
    	notUsingThisAPI: function (unused) {
    		
    	},
        getLocationsByText: function (txt, callback) {
            return getLocationsByText(txt, callback);
        }
    };
})();
