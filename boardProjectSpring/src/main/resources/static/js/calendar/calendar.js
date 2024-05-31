<<<<<<< HEAD
document.addEventListener('DOMContentLoaded', function() {
    let draggableEl = document.getElementById('mydraggable');
=======
console.log("calendarTest.js 연결 확인");

console.log(loginMemberNo);

document.addEventListener('DOMContentLoaded', function() {
    // let draggableEl = document.getElementById('mydraggable');
>>>>>>> 4ab28b8215c49c73b8167e012e936e4c7e20a6cc
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
<<<<<<< HEAD
=======

                

>>>>>>> 4ab28b8215c49c73b8167e012e936e4c7e20a6cc
            }

        }
    });

    calendar.render();
<<<<<<< HEAD
    let draggable = new Draggable(draggableEl);

    new Draggable(containerEl, {
        itemSelector: '.item-class'
    });

    draggable.destroy();
=======
    // let draggable = new Draggable(draggableEl);
    // new Draggable(containerEl, {
    //     itemSelector: '.item-class'
    // });
    
    // draggable.destroy();
    
>>>>>>> 4ab28b8215c49c73b8167e012e936e4c7e20a6cc
});