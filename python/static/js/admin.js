$( document).ready(function() {
    $('.btn-primary').click(function(){
        var data = {
            'title':$('#question_title').val(),
            'answer':$('#answer').val(),
            'option_two':$('#option_two').val(),
            'option_three':$('#option_three').val(),
            'option_four':$('#option_four').val(),
            'category':$('#category').val(),
            'item_id':$('#item_id').val(),
        }
        
        if (data['title'] == '' || data['answer'] == '' || data['option_two'] == '' || data['option_three'] == '' || data['option_four'] == '') {
            alert('کامل کنید')
            return
        }
        $('tr[id="'+data['item_id']+'"]').remove()
        $.ajax({
            url: "/save",
            data:data,
            dataType:"JSON",
        }).done(function(d) {
            if (d['msg'] == "NOKEY") {
                alert('ثبت نشد. یا خالین یا تکراری')
                return
            }
            var cat = ''
            if (data['category']=='1') {
                cat = 'تاریخی'
            } else if (data['category']=='2') {
                cat = 'ورزشی'
            } else if (data['category']=='3') {
                cat = 'هنری'
            } else if (data['category']=='4') {
                cat = 'علمی'
            } else if (data['category']=='5') {
                cat = 'سرگرمی'
            }else if (data['category']=='6') {
                cat = 'جغرافی'
            }
            
            $('.table .first').remove()
            $('.table').prepend('<tr class="first"><td></td><td>گزینه ۴</td><td>گزینه ۳</td><td>گزینه ۲</td><td>جواب</td><td>سوال</td><td></td></tr>' +"<tr id="+ d['_id'] +">"+'<td><button type="button" class="btn btn-warning">X</button><button type="button" class="btn btn-info">E</button></td>'+"<td>" +
                        data['option_four'] + "</td><td> " +
                        data['option_three'] + "</td><td> " +
                        data['option_two'] + "</td><td> " +
                        data['answer'] + "</td><td> " +
                        data['title'] + "</td><td> " +
                        cat + "</td><tr>")
            
            $('#question_title').val('')
            $('#answer').val('')
            $('#option_two').val('')
            $('#option_three').val('')
            $('#option_four').val('')
            $('#item_id').val('')

        });
    })
    
    $('.btn-warning').bind({click: function(){
        var t = $(this).parent().parent()
        $.ajax({
            url: "/delete",
            data:{'_id':t.attr('id')},
            dataType:"JSON",
        }).done(function(d) {
            if (d['msg'] == 'OK') {
                t.remove()
            }
        });
    }})
    
    $('.btn-info').bind({click: function(){
        var t = $(this).parent().parent()
        $.ajax({
            url: "/edit",
            data:{'_id':t.attr('id')},
            dataType:"JSON",
        }).done(function(d) {
            var data = d['data']
            $('#item_id').val(t.attr('id'))
            $('#question_title').val(data['title'])
            $('#answer').val(data['answer'])
            $('#option_two').val(data['option_two'])
            $('#option_three').val(data['option_three'])
            $('#option_four').val(data['option_four'])
            $('#category').val(data['category'])
        });
    }})    
})