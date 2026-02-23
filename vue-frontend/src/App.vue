<template>
  <nav class="navbar navbar-dark mb-4" style="background-color: #0D2137;">
    <div class="container">
      <span class="navbar-brand fw-bold">📚 Bokbibliotek — Spring Boot Demo</span>
      <span class="text-muted small"><code class="text-light">GET/POST/PUT/DELETE → localhost:8080/api/books</code></span>
    </div>
  </nav>

  <div class="container pb-5">

    <!-- Alerts -->
    <div v-if="error" class="alert alert-danger d-flex align-items-center" role="alert">
      <i class="bi bi-exclamation-triangle-fill me-2"></i> {{ error }}
    </div>
    <div v-if="success" class="alert alert-success d-flex align-items-center" role="alert">
      <i class="bi bi-check-circle-fill me-2"></i> {{ success }}
    </div>

    <!-- GET ALL -->
    <div class="card mb-4 shadow-sm">
      <div class="card-header d-flex align-items-center gap-2" style="background-color:#f8f9fa;">
        <span class="badge bg-success">GET</span>
        <strong>Alla böcker</strong>
        <code class="text-muted ms-auto small">/api/books</code>
      </div>
      <div class="card-body">
        <div class="row g-2 mb-3">
          <div class="col-md-4">
            <input v-model="filter.keyword" class="form-control form-control-sm" placeholder="Sök titel..." />
          </div>
          <div class="col-md-4">
            <input v-model="filter.author" class="form-control form-control-sm" placeholder="Filtrera författare..." />
          </div>
          <div class="col-md-2">
            <button class="btn btn-sm btn-success w-100" @click="fetchBooks">
              <i class="bi bi-arrow-clockwise me-1"></i>Hämta
            </button>
          </div>
        </div>

        <div class="table-responsive" v-if="books.length">
          <table class="table table-hover table-bordered table-sm align-middle mb-0">
            <thead class="table-dark">
              <tr>
                <th>ID</th><th>Titel</th><th>Författare</th><th>År</th><th>Beskrivning</th><th>Åtgärder</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="book in books" :key="book.id">
                <td class="text-muted">{{ book.id }}</td>
                <td><strong>{{ book.title }}</strong></td>
                <td>{{ book.author }}</td>
                <td>{{ book.publishedYear }}</td>
                <td class="text-muted small">{{ book.description }}</td>
                <td>
                  <button class="btn btn-warning btn-sm me-1" @click="startEdit(book)">
                    <i class="bi bi-pencil"></i>
                  </button>
                  <button class="btn btn-danger btn-sm" @click="deleteBook(book.id)">
                    <i class="bi bi-trash"></i>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <p v-else-if="fetched" class="text-muted mb-0">Inga böcker hittades.</p>
      </div>
    </div>

    <div class="row g-4">

      <!-- GET BY ID -->
      <div class="col-md-6">
        <div class="card shadow-sm h-100">
          <div class="card-header d-flex align-items-center gap-2" style="background-color:#f8f9fa;">
            <span class="badge bg-success">GET</span>
            <strong>Hämta via ID</strong>
            <code class="text-muted ms-auto small">/api/books/{id}</code>
          </div>
          <div class="card-body">
            <div class="input-group input-group-sm mb-3">
              <input v-model="getById.id" type="number" class="form-control" placeholder="ID" />
              <button class="btn btn-success" @click="fetchById">Hämta</button>
            </div>
            <pre v-if="getById.result" class="bg-light border rounded p-2 small mb-0">{{ getById.result }}</pre>
          </div>
        </div>
      </div>

      <!-- DELETE BY ID -->
      <div class="col-md-6">
        <div class="card shadow-sm h-100">
          <div class="card-header d-flex align-items-center gap-2" style="background-color:#f8f9fa;">
            <span class="badge bg-danger">DELETE</span>
            <strong>Ta bort via ID</strong>
            <code class="text-muted ms-auto small">/api/books/{id}</code>
          </div>
          <div class="card-body">
            <div class="input-group input-group-sm mb-3">
              <input v-model="deleteById.id" type="number" class="form-control" placeholder="ID" />
              <button class="btn btn-danger" @click="deleteBookById">Ta bort</button>
            </div>
            <p v-if="deleteById.result" class="text-muted small mb-0">{{ deleteById.result }}</p>
          </div>
        </div>
      </div>

    </div>

    <!-- POST -->
    <div class="card mt-4 shadow-sm">
      <div class="card-header d-flex align-items-center gap-2" style="background-color:#f8f9fa;">
        <span class="badge bg-primary">POST</span>
        <strong>Skapa bok</strong>
        <code class="text-muted ms-auto small">/api/books</code>
      </div>
      <div class="card-body">
        <div class="row g-2 mb-3">
          <div class="col-md-6">
            <label class="form-label small fw-bold">Titel</label>
            <input v-model="form.title" class="form-control form-control-sm" placeholder="Bokens titel" />
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-bold">Författare</label>
            <input v-model="form.author" class="form-control form-control-sm" placeholder="Författarens namn" />
          </div>
          <div class="col-md-3">
            <label class="form-label small fw-bold">Utgivningsår</label>
            <input v-model.number="form.publishedYear" type="number" class="form-control form-control-sm" />
          </div>
          <div class="col-md-9">
            <label class="form-label small fw-bold">Beskrivning</label>
            <input v-model="form.description" class="form-control form-control-sm" placeholder="Kort beskrivning" />
          </div>
        </div>
        <button class="btn btn-primary btn-sm" @click="createBook">
          <i class="bi bi-plus-circle me-1"></i>Skapa bok
        </button>
        <template v-if="form.result">
          <p class="small fw-bold mt-3 mb-1">Svar från servern (201 Created):</p>
          <pre class="bg-light border rounded p-2 small mb-0">{{ form.result }}</pre>
        </template>
      </div>
    </div>

    <!-- PUT -->
    <div class="card mt-4 shadow-sm border-warning" v-if="editForm.active">
      <div class="card-header d-flex align-items-center gap-2 bg-warning bg-opacity-25">
        <span class="badge bg-warning text-dark">PUT</span>
        <strong>Uppdatera bok</strong>
        <span class="badge bg-secondary ms-1">ID: {{ editForm.id }}</span>
        <code class="text-muted ms-auto small">/api/books/{{ editForm.id }}</code>
      </div>
      <div class="card-body">
        <div class="row g-2 mb-3">
          <div class="col-md-6">
            <label class="form-label small fw-bold">Titel</label>
            <input v-model="editForm.title" class="form-control form-control-sm" />
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-bold">Författare</label>
            <input v-model="editForm.author" class="form-control form-control-sm" />
          </div>
          <div class="col-md-3">
            <label class="form-label small fw-bold">Utgivningsår</label>
            <input v-model.number="editForm.publishedYear" type="number" class="form-control form-control-sm" />
          </div>
          <div class="col-md-9">
            <label class="form-label small fw-bold">Beskrivning</label>
            <input v-model="editForm.description" class="form-control form-control-sm" />
          </div>
        </div>
        <button class="btn btn-warning btn-sm me-2" @click="updateBook">
          <i class="bi bi-save me-1"></i>Spara
        </button>
        <button class="btn btn-outline-secondary btn-sm" @click="editForm.active = false">Avbryt</button>
        <template v-if="editForm.result">
          <p class="small fw-bold mt-3 mb-1">Svar från servern (200 OK):</p>
          <pre class="bg-light border rounded p-2 small mb-0">{{ editForm.result }}</pre>
        </template>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

const BASE = '/api/books'

const books   = ref([])
const fetched = ref(false)
const error   = ref('')
const success = ref('')

const filter     = reactive({ keyword: '', author: '' })
const getById    = reactive({ id: '', result: '' })
const deleteById = reactive({ id: '', result: '' })
const form       = reactive({ title: '', author: '', publishedYear: new Date().getFullYear(), description: '', result: '' })
const editForm   = reactive({ active: false, id: null, title: '', author: '', publishedYear: 0, description: '', result: '' })

function flash(msg, isError = false) {
  error.value   = isError ? msg : ''
  success.value = isError ? '' : msg
  setTimeout(() => { error.value = ''; success.value = '' }, 3000)
}

async function api(path = '', options = {}) {
  const res = await fetch(BASE + path, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  })
  if (!res.ok) {
    const body = await res.json().catch(() => ({}))
    throw new Error(body.message || body.error || `HTTP ${res.status}`)
  }
  if (res.status === 204) return null
  return res.json()
}

async function fetchBooks() {
  try {
    let q = ''
    if (filter.keyword) q = `?keyword=${encodeURIComponent(filter.keyword)}`
    else if (filter.author) q = `?author=${encodeURIComponent(filter.author)}`
    books.value = await api(q)
    fetched.value = true
    flash(`${books.value.length} bok(böcker) hämtade`)
  } catch (e) { flash(e.message, true) }
}

async function fetchById() {
  try {
    const data = await api(`/${getById.id}`)
    getById.result = JSON.stringify(data, null, 2)
  } catch (e) { getById.result = 'Fel: ' + e.message }
}

async function createBook() {
  try {
    const data = await api('', {
      method: 'POST',
      body: JSON.stringify({ title: form.title, author: form.author, publishedYear: form.publishedYear, description: form.description })
    })
    form.result = JSON.stringify(data, null, 2)
    flash(`Bok skapad med ID ${data.id}`)
    form.title = ''
    form.author = ''
    form.publishedYear = new Date().getFullYear()
    form.description = ''
    fetchBooks()
  } catch (e) { flash(e.message, true) }
}

function startEdit(book) {
  Object.assign(editForm, { ...book, active: true, result: '' })
  window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' })
}

async function updateBook() {
  try {
    const data = await api(`/${editForm.id}`, {
      method: 'PUT',
      body: JSON.stringify({ title: editForm.title, author: editForm.author, publishedYear: editForm.publishedYear, description: editForm.description })
    })
    editForm.result = JSON.stringify(data, null, 2)
    flash(`Bok ${data.id} uppdaterad`)
    fetchBooks()
  } catch (e) { flash(e.message, true) }
}

async function deleteBook(id) {
  if (!confirm(`Ta bort bok med ID ${id}?`)) return
  try {
    await api(`/${id}`, { method: 'DELETE' })
    flash(`Bok ${id} borttagen`)
    fetchBooks()
  } catch (e) { flash(e.message, true) }
}

async function deleteBookById() {
  if (!confirm(`Ta bort bok med ID ${deleteById.id}?`)) return
  try {
    await api(`/${deleteById.id}`, { method: 'DELETE' })
    deleteById.result = `Bok ${deleteById.id} borttagen ✅`
    flash(`Bok ${deleteById.id} borttagen`)
    fetchBooks()
  } catch (e) { deleteById.result = 'Fel: ' + e.message }
}

onMounted(fetchBooks)
</script>