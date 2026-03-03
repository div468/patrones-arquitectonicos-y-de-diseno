const baseUrl = "http://localhost:8080/api/animales";

const form = document.getElementById("animalForm");
const lista = document.getElementById("lista");
const btnListar = document.getElementById("btnListar");
const btnOrdenar = document.getElementById("btnOrdenar");
const btnLimpiar = document.getElementById("btnLimpiar");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const animal = {
        nombre: document.getElementById("nombre").value,
        edad: parseInt(document.getElementById("edad").value, 10),
        tipo: document.getElementById("tipo").value,
        habitat: document.getElementById("habitat").value
    };

    const response = await fetch(baseUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(animal)
    });

    const mensaje = await response.text();
    alert(mensaje);

    if (response.ok) {
        form.reset();
        await listar();
    }
});

btnListar.addEventListener("click", () => listar());
btnOrdenar.addEventListener("click", () => listar("edad"));
btnLimpiar.addEventListener("click", () => {
    lista.innerHTML = "";
});

async function listar(criterio) {
    const criterioValido = typeof criterio === "string" && criterio.trim().length > 0;

    const url = criterioValido
        ? `${baseUrl}/ordenar?criterio=${encodeURIComponent(criterio)}`
        : baseUrl;

    const response = await fetch(url);
    const data = await response.json();

    lista.innerHTML = "";

    if (!Array.isArray(data) || data.length === 0) {
        lista.innerHTML = "<li>No hay animales registrados</li>";
        return;
    }

    data.forEach((animal) => {
        const item = document.createElement("li");
        item.textContent = `${animal.tipo} | ${animal.nombre} | ${animal.edad} años | ${animal.habitat}`;
        lista.appendChild(item);
    });
}
