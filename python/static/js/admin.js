$( document).ready(function() {
    $('.btn-primary').click(function(){
        var data = {
            'title':$('#question_title').val(),
            'answer':$('#answer').val(),
            'option_two':$('#option_two').val(),
            'option_three':$('#option_three').val(),
            'option_four':$('#option_four').val(),
            'category':$('#category').val(),
            'exam_year':$('#exam_year').val(),
            'exam_category':$('#exam_category').val(),
            'exam_field_of_study':$('#exam_field_of_study').val(),
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
            }else if (data['category']=='1001') {
                cat = 'ادبیات'
            }else if (data['category']=='1002') {
                cat = 'عربی'
            }else if (data['category']=='1003') {
                cat = 'دین و زندگی'
            }else if (data['category']=='1004') {
                cat = 'زبان'
            }
            
            var cat2 = ''
            if (data['exam_category']=='1') {
                cat2 = 'سراسری'
            } else if (data['exam_category']=='2') {
                cat2 = 'سراسری خارج از کشور'
            } else if (data['exam_category']=='3') {
                cat2 = 'آزاد'
            }
            
            var cat3 = ''
            if (data['exam_field_of_study']=='1') {
                cat3 = 'ریاضی'
            } else if (data['exam_field_of_study']=='2') {
                cat3 = 'تجربی'
            } else if (data['exam_field_of_study']=='3') {
                cat3 = 'انسانی'
            } else if (data['exam_field_of_study']=='4') {
                cat3 = 'زبان'
            } else if (data['exam_field_of_study']=='5') {
                cat3 = 'هنر'
            }
            
            var cat4 = ''
            if (data['exam_year']=='93') {
                cat4 = '93'
            } else if (data['exam_year']=='92') {
                cat4 = '92'
            } else if (data['exam_year']=='91') {
                cat4 = '91'
            } else if (data['exam_year']=='90') {
                cat4 = '90'
            } else if (data['exam_year']=='89') {
                cat4 = '89'
            } else if (data['exam_year']=='88') {
                cat4 = '88'
            } else if (data['exam_year']=='87') {
                cat4 = '87'
            } else if (data['exam_year']=='86') {
                cat4 = '86'
            } else if (data['exam_year']=='85') {
                cat4 = '85'
            }
            
            $('.table .first').remove()
            $('.table').prepend('<tr class="first"><td></td><td>سال</td><td>رشته</td><td>نوع</td><td>گزینه ۴</td><td>گزینه ۳</td><td>گزینه ۲</td><td>جواب</td><td>سوال</td><td></td></tr>' +"<tr id="+ d['_id'] +">"+'<td><button type="button" class="btn btn-warning">X</button><button type="button" class="btn btn-info">E</button></td>'+"<td>" +
                        cat4 + "</td><td> " +
                        cat3 + "</td><td> " +
                        cat2 + "</td><td> " +
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
            $('#exam_year').val(data['exam_year'])
            $('#exam_field_of_study').val(data['exam_field_of_study'])
            $('#exam_category').val(data['exam_category'])
        });
    }})    
})