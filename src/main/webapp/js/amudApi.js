window.twitterApi = (function () {
    var throttleFunction = function (fn, throttleMilliseconds) {
        var invocationTimeout;

        return function () {
            var args = arguments;
            if (invocationTimeout)
                clearTimeout(invocationTimeout);

            invocationTimeout = setTimeout(function () {
                invocationTimeout = null;
                fn.apply(window, args);
            }, throttleMilliseconds);
        };
    };

    var getTweetsForUsersThrottled = throttleFunction(function (userNames, callback) {
        if (userNames.length == 0)
            callback([]);
        else {
            var url = "/GetLocationsByText?txt=" + userNames[0];
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
    }, 50);

    return {
        getTweetsForUser: function (userName, callback) {
            return this.getTweetsForUsers([userName], callback);
        },
        getTweetsForUsers: function (userNames, callback) {
            return getTweetsForUsersThrottled(userNames, callback);
        }
    };
})();
