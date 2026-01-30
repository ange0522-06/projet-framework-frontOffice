<template>
  <div class="users">
    <h1>User Management</h1>
    
    <!-- User Form -->
    <div class="user-form">
      <h2>{{ editingUser ? 'Edit User' : 'Add New User' }}</h2>
      <form @submit.prevent="saveUser">
        <div class="form-group">
          <label>Name:</label>
          <input v-model="currentUser.name" type="text" required />
        </div>
        <div class="form-group">
          <label>Email:</label>
          <input v-model="currentUser.email" type="email" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn-save">{{ editingUser ? 'Update' : 'Create' }}</button>
          <button type="button" @click="resetForm" class="btn-cancel">Cancel</button>
        </div>
      </form>
    </div>

    <!-- User List -->
    <div class="user-list">
      <h2>Users List</h2>
      <div v-if="loading" class="loading">Loading...</div>
      <div v-else-if="users.length === 0" class="no-users">No users found.</div>
      <table v-else>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.email }}</td>
            <td>
              <button @click="editUser(user)" class="btn-edit">Edit</button>
              <button @click="deleteUserById(user.id)" class="btn-delete">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import api from '../services/api'

export default {
  name: 'Users',
  data() {
    return {
      users: [],
      currentUser: {
        name: '',
        email: ''
      },
      editingUser: null,
      loading: false
    }
  },
  mounted() {
    this.fetchUsers()
  },
  methods: {
    async fetchUsers() {
      this.loading = true
      try {
        const response = await api.getUsers()
        this.users = response.data
      } catch (error) {
        console.error('Error fetching users:', error)
        alert('Failed to fetch users. Make sure the backend is running on port 8080.')
      } finally {
        this.loading = false
      }
    },
    async saveUser() {
      try {
        if (this.editingUser) {
          await api.updateUser(this.editingUser.id, this.currentUser)
        } else {
          await api.createUser(this.currentUser)
        }
        this.resetForm()
        this.fetchUsers()
      } catch (error) {
        console.error('Error saving user:', error)
        alert('Failed to save user.')
      }
    },
    editUser(user) {
      this.editingUser = user
      this.currentUser = { ...user }
    },
    async deleteUserById(id) {
      if (confirm('Are you sure you want to delete this user?')) {
        try {
          await api.deleteUser(id)
          this.fetchUsers()
        } catch (error) {
          console.error('Error deleting user:', error)
          alert('Failed to delete user.')
        }
      }
    },
    resetForm() {
      this.currentUser = { name: '', email: '' }
      this.editingUser = null
    }
  }
}
</script>

<style scoped>
.users {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.user-form {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-actions {
  margin-top: 20px;
}

.btn-save, .btn-cancel, .btn-edit, .btn-delete {
  padding: 8px 16px;
  margin-right: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-save {
  background-color: #42b983;
  color: white;
}

.btn-save:hover {
  background-color: #359268;
}

.btn-cancel {
  background-color: #ccc;
  color: #333;
}

.btn-cancel:hover {
  background-color: #bbb;
}

.btn-edit {
  background-color: #4CAF50;
  color: white;
}

.btn-edit:hover {
  background-color: #45a049;
}

.btn-delete {
  background-color: #f44336;
  color: white;
}

.btn-delete:hover {
  background-color: #da190b;
}

.user-list {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background-color: #42b983;
  color: white;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

tbody tr:hover {
  background-color: #f5f5f5;
}

.loading, .no-users {
  padding: 20px;
  text-align: center;
  color: #666;
}
</style>
