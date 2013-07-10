window.twitterApi = (function () {

    var getLocationsByText = function (txt, callback) {
        if (txt.length == 0)
            callback([]);
        else {
            var url = "/GetLocationsByText?txt=" + txt;
            $.ajax({
                url: url,
                dataType: "json",
                success: function (data) { 
                	var ret = $.grep(data.results || [], function (tweet) {
                		return tweet.name; 
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
        getTweetsForUser: function (userName, callback) {
            return this.getLocationsByText([userName], callback);
        },
        getLocationsByText: function (txt, callback) {
            return getLocationsByText(txt, callback);
        }
    };
})();
