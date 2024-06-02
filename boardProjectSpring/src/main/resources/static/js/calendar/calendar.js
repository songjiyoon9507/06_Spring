console.log("calendarTest.js 연결 확인");

document.addEventListener('DOMContentLoaded', function() {

    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        selectable: true,
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        timeZone: 'UTC',
        droppable: true,
        dayMaxEvents: true,
        // events: 'https://fullcalendar.io/api/demo-feeds/events.json?overload-day',
        events: 'https://fullcalendar.io/api/demo-feeds/events.json',
        select: function(info) {
            alert('selected' + info.startStr + ' to ' + info.endStr);
            var title = prompt('Enter event title:');
            var titleColor = prompt('Enter titleColor:');
            var requestMember = prompt('일정에 포함된 사람을 입력해주세요.');
            if (title) {
                calendar.addEvent({
                    title: title,
                    start: info.startStr,
                    end: info.endStr,
                    description : requestMember,
                    color: titleColor
                });

            }

        },
        eventClick: function(info) {
            $('#eventModalLabel').text(info.event.title);
            $('#eventDescription').text(info.event.extendedProps.description);
            $('#eventModal').modal('show');
        }
    });

    calendar.render();

});