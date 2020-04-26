var selectedRow = null

function onFormSubmit() {
    if (validate()) {
        var formData = readFormData();
        if (selectedRow == null)
            insertNewRecord(formData);
        else
            updateRecord(formData);
        resetForm();
    }
}

function readFormData() {
    var formData = {};
    formData["id"] = document.getElementById("id").value;
    formData["login"] = document.getElementById("login").value;
    formData["password"] = document.getElementById("password").value;
    formData["role"] = document.getElementById("role").value;
    return formData;
}

function insertNewRecord(data) {
    var table = document.getElementById("datatable")//это table id в хтмл
        .getElementsByTagName('tbody')[0];// сделать этот тег пустым
    var newRow = table.insertRow(table.length);
    cell1 = newRow.insertCell(0);
    cell1.innerHTML = data.id;
    cell2 = newRow.insertCell(1);
    cell2.innerHTML = data.login;
    cell3 = newRow.insertCell(2);
    cell3.innerHTML = data.password;
    cell4 = newRow.insertCell(3);
    cell4.innerHTML = data.role;
    cell4 = newRow.insertCell(4);
    cell4.innerHTML = `<a onClick="onEdit(this)">Edit</a>
                       <a onClick="onDelete(this)">Delete</a>`;
}

function resetForm() {
    document.getElementById("id").value = "";
    document.getElementById("login").value = "";
    document.getElementById("password").value = "";
    document.getElementById("role").value = "";
    selectedRow = null;
}

function onEdit(td) {
    selectedRow = td.parentElement.parentElement;
    document.getElementById("id").value = selectedRow.cells[0].innerHTML
    document.getElementById("login").value = selectedRow.cells[1].innerHTML;
    document.getElementById("password").value = selectedRow.cells[2].innerHTML;
    document.getElementById("role").value = selectedRow.cells[3].innerHTML;
}
function updateRecord(formData) {
    selectedRow.cells[0].innerHTML = formData.id;
    selectedRow.cells[1].innerHTML = formData.login;
    selectedRow.cells[2].innerHTML = formData.password;
    selectedRow.cells[3].innerHTML = formData.role;
}

function onDelete(td) {
    if (confirm('Are you sure to delete this record ?')) {
        row = td.parentElement.parentElement;
        document.getElementById("datatable").deleteRow(row.rowIndex);
        resetForm();
    }
}
function validate() {
    isValid = true;
    if (document.getElementById("id").value == "") {
        isValid = false;
        document.getElementById("idValidationError").classList.remove("hide");
    } else {
        isValid = true;
        if (!document.getElementById("idValidationError").classList.contains("hide"))
            document.getElementById("idValidationError").classList.add("hide");
    }
    return isValid;
}