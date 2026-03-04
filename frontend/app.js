const BASE_URL = "http://localhost:8080/api/animales";

// ─── Datos de referencia (espejo del backend) ─────────────────────────────────

const ANIMAL_META = {
  LEON:      { emoji: "🦁", sonido: "Rugido" },
  ELEFANTE:  { emoji: "🐘", sonido: "Barrito" },
  MONO:      { emoji: "🐒", sonido: "Chillido" },
  TIBURON:   { emoji: "🦈", sonido: "Silencio" },
  AGUILA:    { emoji: "🦅", sonido: "Chillido agudo" },
  COCODRILO: { emoji: "🐊", sonido: "Gruñido" },
};

// Misma tabla de compatibilidad que ValidarCompatibilidad.java
const COMPATIBILIDAD = {
  LEON:      ["SABANA", "DESIERTO"],
  ELEFANTE:  ["SABANA", "SELVA"],
  MONO:      ["SELVA"],
  TIBURON:   ["ACUARIO", "RIO"],
  AGUILA:    ["MONTANA"],
  COCODRILO: ["RIO", "SELVA"],
};

const HABITAT_LABEL = {
  SABANA:   "Sabana",
  SELVA:    "Selva",
  ACUARIO:  "Acuario",
  RIO:      "Río",
  DESIERTO: "Desierto",
  MONTANA:  "Montaña",
};

// ─── Hint de compatibilidad en tiempo real ────────────────────────────────────

const tipoSelect    = document.getElementById("tipo");
const habitatSelect = document.getElementById("habitat");
const compatHint    = document.getElementById("compatHint");

function actualizarCompatHint() {
  const tipo    = tipoSelect.value;
  const habitat = habitatSelect.value;
  const validos = COMPATIBILIDAD[tipo] || [];
  const lista   = validos.map(h => HABITAT_LABEL[h]).join(", ");

  if (validos.includes(habitat)) {
    compatHint.textContent = `✓ Compatible · ${tipo} puede vivir en: ${lista}`;
    compatHint.className = "compat-hint ok";
  } else {
    compatHint.textContent = `✗ Incompatible · ${tipo} solo puede vivir en: ${lista}`;
    compatHint.className = "compat-hint err";
  }
}

tipoSelect.addEventListener("change", actualizarCompatHint);
habitatSelect.addEventListener("change", actualizarCompatHint);
actualizarCompatHint();

// ─── Tooltips flotantes ───────────────────────────────────────────────────────

let activeTooltip = null;

document.querySelectorAll(".tooltip-trigger").forEach(trigger => {
  trigger.addEventListener("mouseenter", () => {
    const tooltip = document.getElementById("tooltip-" + trigger.dataset.tooltip);
    if (!tooltip) return;
    if (activeTooltip && activeTooltip !== tooltip) activeTooltip.classList.remove("visible");

    const rect = trigger.getBoundingClientRect();
    tooltip.style.top  = (rect.bottom + window.scrollY + 8) + "px";
    tooltip.style.left = Math.min(rect.left, window.innerWidth - 320) + "px";
    tooltip.classList.add("visible");
    activeTooltip = tooltip;
  });

  trigger.addEventListener("mouseleave", () => {
    setTimeout(() => {
      if (activeTooltip) { activeTooltip.classList.remove("visible"); activeTooltip = null; }
    }, 200);
  });
});

// ─── Chain of Responsibility: animación de los 5 eslabones ───────────────────

const CHAIN_STEPS = ["v-nombre", "v-edad", "v-tipo", "v-habitat", "v-compat"];

function resetChain() {
  CHAIN_STEPS.forEach(id => {
    document.getElementById(id).className =
      id === "v-compat" ? "chain__step chain__step--highlight" : "chain__step";
  });
  setChainStatus("", "");
}

async function runChainAnimation(nombre, edad, tipo, habitat) {
  resetChain();

  const validaciones = [
    {
      id: "v-nombre", field: "nombre",
      check: () => {
        if (!nombre.trim()) return "El nombre no puede estar vacío";
        if (nombre.trim().length > 30) return "Máximo 30 caracteres";
        if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/.test(nombre.trim())) return "Solo se permiten letras";
        return null;
      },
    },
    {
      id: "v-edad", field: "edad",
      check: () => {
        const n = parseInt(edad);
        if (isNaN(n) || n < 1) return "La edad debe ser al menos 1";
        if (n > 80)            return "La edad no puede superar 80";
        return null;
      },
    },
    {
      id: "v-tipo", field: "tipo",
      check: () => COMPATIBILIDAD[tipo] ? null : "Tipo de animal no reconocido",
    },
    {
      id: "v-habitat", field: "habitat",
      check: () => HABITAT_LABEL[habitat] ? null : "Hábitat no reconocido",
    },
    {
      id: "v-compat", field: "habitat",
      check: () => {
        const validos = COMPATIBILIDAD[tipo] || [];
        if (!validos.includes(habitat)) {
          const lista = validos.map(h => HABITAT_LABEL[h]).join(", ");
          return `${tipo} no puede vivir en ${HABITAT_LABEL[habitat] || habitat}. Válidos: ${lista}`;
        }
        return null;
      },
    },
  ];

  for (const v of validaciones) {
    const el       = document.getElementById(v.id);
    const base     = v.id === "v-compat" ? "chain__step chain__step--highlight" : "chain__step";
    el.className   = base + " pending";
    await delay(320);

    const error = v.check();
    if (error) {
      el.className = base + " invalid";
      const input = document.getElementById(v.field);
      if (input) {
        input.classList.add("error");
        ["input", "change"].forEach(ev =>
          input.addEventListener(ev, () => input.classList.remove("error"), { once: true })
        );
      }
      setChainStatus("✗ " + error, "err");
      return false;
    }
    el.className = base + " valid";
  }

  setChainStatus("✓ Todas las validaciones pasaron — registrando…", "ok");
  return true;
}

function setChainStatus(msg, cls) {
  const el = document.getElementById("chainStatus");
  el.textContent = msg;
  el.className = "chain-status " + cls;
}

function delay(ms) { return new Promise(r => setTimeout(r, ms)); }

// ─── Registrar ────────────────────────────────────────────────────────────────

document.getElementById("btnRegistrar").addEventListener("click", async () => {
  const nombre  = document.getElementById("nombre").value;
  const edad    = document.getElementById("edad").value;
  const tipo    = tipoSelect.value;
  const habitat = habitatSelect.value;

  const ok = await runChainAnimation(nombre, edad, tipo, habitat);
  if (!ok) return;

  try {
    const response = await fetch(BASE_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nombre, edad: parseInt(edad), tipo, habitat }),
    });

    if (response.ok) {
      document.getElementById("nombre").value = "";
      document.getElementById("edad").value   = "";
      resetChain();
      actualizarCompatHint();
      await listar();
    } else {
      const msg = await response.text();
      setChainStatus("✗ Servidor: " + msg, "err");
    }
  } catch {
    setChainStatus("✗ No se pudo conectar al servidor (¿está corriendo Spring Boot?)", "err");
  }
});

// ─── Listar / Ordenar ─────────────────────────────────────────────────────────

document.getElementById("btnListar").addEventListener("click",        () => listar());
document.getElementById("btnOrdenarEdad").addEventListener("click",   () => listar("edad"));
document.getElementById("btnOrdenarNombre").addEventListener("click", () => listar("nombre"));
document.getElementById("btnLimpiar").addEventListener("click", () => {
  document.getElementById("animalGrid").innerHTML =
    `<div class="empty-state"><span>🦒</span><p>Registra tu primer animal</p></div>`;
});

async function listar(criterio) {
  const url = criterio ? `${BASE_URL}/ordenar?criterio=${criterio}` : BASE_URL;
  try {
    const response = await fetch(url);
    if (!response.ok) return;
    renderAnimales(await response.json());
  } catch {
    document.getElementById("animalGrid").innerHTML =
      `<div class="empty-state"><span>⚠️</span><p>No se pudo conectar al servidor</p></div>`;
  }
}

// ─── Render ───────────────────────────────────────────────────────────────────

function renderAnimales(animales) {
  const grid = document.getElementById("animalGrid");
  if (!animales.length) {
    grid.innerHTML = `<div class="empty-state"><span>🦒</span><p>No hay animales registrados</p></div>`;
    return;
  }
  grid.innerHTML = "";
  animales.forEach(animal => {
    const meta = ANIMAL_META[animal.tipo?.toUpperCase()] || { emoji: "🐾", sonido: "?" };
    const card = document.createElement("div");
    card.className = "animal-card";
    card.innerHTML = `
      <div class="animal-card__emoji">${meta.emoji}</div>
      <div class="animal-card__info">
        <div class="animal-card__name">${animal.nombre}</div>
        <div class="animal-card__meta">
          <span>🗓 ${animal.edad} año${animal.edad !== 1 ? "s" : ""}</span>
          <span class="habitat-bridge" title="Bridge: Habitat.describir()">
            ${animal.habitat}
          </span>
        </div>
      </div>
      <div class="animal-card__sound">
        <span class="sound-label">hacerSonido()</span>
        <span class="sound-value">${meta.sonido}</span>
      </div>
    `;
    grid.appendChild(card);
  });
}