/**
 * Created by franc on 5/16/2016.
 */
window.storage = $.sessionStorage;
window.nameInStorage = 'last-sync';
$(document).on('ready', function () {
    $('#sync').click(function () {
        console.log('i was here');
        $.when(sync()).done(function() {
            storage.set(nameInStorage, new Date());
        });
    });
    syncPoll();
});

function syncPoll() {
    if (storage.isSet(nameInStorage)) {
        var date = new Date(storage.get(nameInStorage));
        date.setMinutes(date.getMinutes() + 5);
        if (date <= new Date()) {
            $.when(sync()).then(function() {
                storage.set(nameInStorage, new Date());
                setTimeout(function() {
                    syncPoll()
                }, 5000);
            }, function() {
                setTimeout(function() {
                    syncPoll()
                }, 5000);
            });
        } else {
            setTimeout(function() {
                syncPoll()
            }, 5000);
        }
    } else {
        storage.set(nameInStorage, new Date());
        setTimeout(function() {
            syncPoll()
        }, 5000);
    }
}

function sync() {
    var promise = $.ajax({
        method: 'GET',
        url: '/sync',
        beforeSend: function() {
            $('#sync').html('<p id="sync-nav"><i class="fa fa-refresh fa-spin" aria-hidden="true"></i> Syncing . . .</p>');
        }
    });
    $.when(promise).done(function() {
        $('#sync').html('<p id="sync-nav"><i class="fa fa-refresh" aria-hidden="true"></i> Sync</p>');
    });
    return promise;
}