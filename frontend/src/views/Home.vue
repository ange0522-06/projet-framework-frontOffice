<template>
  <div class="home">
    <h1>Welcome to Vue.js + Spring Boot Framework</h1>
    <p>This is a full-stack application framework with:</p>
    <ul>
      <li>Spring Boot backend with REST API</li>
      <li>Vue.js frontend with Vue Router</li>
      <li>CORS configuration for seamless integration</li>
      <li>H2 database for development</li>
      <li>Sample CRUD operations</li>
    </ul>
    <p>Navigate to <router-link to="/users">Users</router-link> to see the demo.</p>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.home {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  color: #42b983;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin: 10px 0;
  padding: 10px;
  background-color: #f4f4f4;
  border-radius: 5px;
}
</style>
