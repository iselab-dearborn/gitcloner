
const tasks = new Tasks();

const projects = {};

function updateProgress(payload){

    var message = JSON.parse(payload.body);

    const project = projects[message.id];

    if (project) {

        const progress = message.progress;

        if (progress == -1) {
            project.$description.addClass("text-danger");
            project.$description.text(message.description);
        } else {

            if (progress >= project.progress) {
                project.$description.text(message.description);
                project.$progressBar.text(`${progress}%`);
                project.$progressBar.css("width", `${progress}%`);
                project.$progressBar.attr("aria-valuenow", progress);
            }

            if (progress == 100) {
                project.$description.addClass("text-success");
            }
        }

        project.progress = progress;
    }
}

$(function(){

    feather.replace();

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').addClass("slide").toggleClass('active');
        $('#content').addClass("slide").toggleClass('active');
    });

    $(document).ready(function () {
        bsCustomFileInput.init()
    });

    $(".btn-submit").click(function(){
       $(this).parent().submit();
    });

    $(".project").each(function(){

        const projectId = $(this).data("id");

        projects[projectId] = {
            progress: 0,
            $description: $($(this).find(".description")[0]),
            $progressBar: $($(this).find(".progress-bar")[0])
        }
    });

    tasks.on("create", (payload) => {

        updateProgress(payload);
    });

    tasks.on("update", (payload) => {

        updateProgress(payload);
    });

    tasks.on("done", (payload) => {

        updateProgress(payload);
    });

    tasks.on("error", (payload) => {

        updateProgress(payload);
    });
})
