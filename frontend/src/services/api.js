import axios from 'axios'

const API_URL = '/api'

export default {
  // Get all users
  getUsers() {
    return axios.get(`${API_URL}/users`)
  },
  
  // Get user by ID
  getUser(id) {
    return axios.get(`${API_URL}/users/${id}`)
  },
  
  // Create new user
  createUser(user) {
    return axios.post(`${API_URL}/users`, user)
  },
  
  // Update user
  updateUser(id, user) {
    return axios.put(`${API_URL}/users/${id}`, user)
  },
  
  // Delete user
  deleteUser(id) {
    return axios.delete(`${API_URL}/users/${id}`)
  }
}
