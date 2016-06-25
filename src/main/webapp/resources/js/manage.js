/**
 * Created by franc on 5/22/2016.
 */
$(document).on('ready', function() {
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    });
    editEmail();
    editPassword();
    editFirstName();
    editLastName();
    editOrganization();
});

function editEmail() {
    var editEmail = $('#edit-email');
    editEmail.hide();
    editEmail.find('[id^="msg-"]').hide();
    var showEmail = $('#show-edit-email');
    showEmail.click(function() {
        $(this).hide();
        editEmail.show();
        editEmail.parent().addClass('manage');
    });
    $('#cancel-edit-email').click(function() {
        editEmail.hide();
        editEmail.parent().removeClass('manage');
        showEmail.show();
    });
    editEmail.submit(function(e) {
        e.preventDefault();
        editEmail.find('[id^="msg-"]').hide();
        $.when(submitForm(editEmail, '/profile/manage/email')).then(function(response) {
            window.location.href='/profile';
        }, function(response) {
            var responseJson = response.responseJSON;
            $.each(responseJson, function(key, value) {
                var helpId = '#msg-edit-email-'+ key.replace('.', '-');
                var helpSpan = editEmail.find(helpId);
                helpSpan.text(value);
                helpSpan.show();
            });
        });
    });
}

function editPassword() {
    var editPassword = $('#edit-password');
    editPassword.hide();
    var showPassword = $('#show-edit-password');
    showPassword.click(function() {
        $(this).hide();
        editPassword.show();
        editPassword.parent().addClass('manage');
    });
    $('#cancel-edit-password').click(function() {
        editPassword.hide();
        editPassword.parent().removeClass('manage');
        showPassword.show();
    });
    editPassword.submit(function(e) {
        e.preventDefault();
        editPassword.find('[id^="msg-"]').hide();
        $.when(submitForm(editPassword, '/profile/manage/password')).then(function(response) {
            window.location.href='/profile';
        }, function(response) {
            var responseJson = response.responseJSON;
            $.each(responseJson, function(key, value) {
                var helpId = '#msg-edit-password-' + key.replace('.', '-');
                var helpSpan = editPassword.find(helpId);
                helpSpan.text(value);
                helpSpan.show();
            });
        });
    });
}

function editFirstName() {
    var editFirstName = $('#edit-firstname');
    editFirstName.hide();
    var showFirstName = $('#show-edit-firstname');
    showFirstName.click(function() {
        $(this).hide();
        editFirstName.show();
        editFirstName.parent().addClass('manage');
    });
    $('#cancel-edit-firstname').click(function() {
        editFirstName.hide();
        editFirstName.parent().removeClass('manage');
        showFirstName.show();
    });
    editFirstName.submit(function(e) {
        e.preventDefault();
        editFirstName.find('[id^="msg-"]').hide();
        $.when(submitForm(editFirstName, '/profile/manage/firstname')).then(function(response) {
            window.location.href='/profile';
        }, function(response) {
            var responseJson = response.responseJSON;
            $.each(responseJson, function(key, value) {
                var helpId = '#msg-edit-firstname-' + key.replace('.', '-');
                var helpSpan = editFirstName.find(helpId);
                helpSpan.text(value);
                helpSpan.show();
            });
        });
    });
}

function editLastName() {
    var editLastName = $('#edit-lastname');
    editLastName.hide();
    var showLastName = $('#show-edit-lastname');
    showLastName.click(function() {
        $(this).hide();
        editLastName.show();
        editLastName.parent().addClass('manage');
    });
    $('#cancel-edit-lastname').click(function() {
        editLastName.hide();
        editLastName.parent().removeClass('manage');
        showLastName.show();
    });
    editLastName.submit(function(e) {
        e.preventDefault();
        editLastName.find('[id^="msg-"]').hide();
        $.when(submitForm(editLastName, '/profile/manage/lastname')).then(function(response) {
            window.location.href='/profile';
        }, function(response) {
            var responseJson = response.responseJSON;
            $.each(responseJson, function(key, value) {
                var helpId = '#msg-edit-lastname-' + key.replace('.', '-');
                var helpSpan = editLastName.find(helpId);
                helpSpan.text(value);
                helpSpan.show();
            });
        });
    });
}

function editOrganization() {
    var editOrganization = $('#edit-organization');
    editOrganization.hide();
    var showOrganization = $('#show-edit-organization');
    showOrganization.click(function() {
        $(this).hide();
        editOrganization.show();
        editOrganization.parent().addClass('manage');
    });
    $('#cancel-edit-organization').click(function() {
        editOrganization.hide();
        editOrganization.parent().removeClass('manage');
        showOrganization.show();
    });
    editOrganization.submit(function(e) {
        e.preventDefault();
        editOrganization.find('[id^="msg-"]').hide();
        $.when(submitForm(editOrganization, '/profile/manage/organization')).then(function(response) {
            window.location.href='/profile';
        }, function(response) {
            var responseJson = response.responseJSON;
            $.each(responseJson, function(key, value) {
                var helpId = '#msg-edit-organization-' + key.replace('.', '-');
                var helpSpan = editOrganization.find(helpId);
                helpSpan.text(value);
                helpSpan.show();
            });
        });
    });
}

function submitForm(form, url) {
    var data = form.serializeJSON();
    return $.ajax({
        method: 'POST',
        url: url,
        headers: {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json'
        },
        data: JSON.stringify(data)
    });
}