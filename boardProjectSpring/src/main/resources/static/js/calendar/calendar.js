document.addEventListener('DOMContentLoaded', function() {
    let draggableEl = document.getElementById('mydraggable');
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
        events: 'https://fullcalendar.io/api/demo-feeds/events.json?overload-day',
        events: 'https://fullcalendar.io/api/demo-feeds/events.json',
        select: function(info) {
            alert('selected' + info.startStr + ' to ' + info.endStr);
            var title = prompt('Enter event title:');
            if (title) {
                calendar.addEvent({
                    title: title,
                    start: info.startStr,
                    end: info.endStr
                });
            }

        }
    });

    calendar.render();
    let draggable = new Draggable(draggableEl);

    new Draggable(containerEl, {
        itemSelector: '.item-class'
    });

    draggable.destroy();
});