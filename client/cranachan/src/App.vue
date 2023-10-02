<template>
  <div id="app">
    <div class="content" >
      <header class="navbar navbar-expand-lg navbar-dark bg-dark">
      
        <div class="container-fluid">
          <a class="navbar-brand" href="#"><img src="./assets/jhi-white.png" height="60" alt="James Hutton Institute Logo"></a>
          <div class="navbar-text align-items-center">
            <h1>Cranachan</h1>
          </div>
        </div>

      </header>

      <main>
        <router-view/>
      </main>
    </div>
  </div>
</template>
<script>

export default {
  name: 'App',

}
</script>

<style>
#app {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: left;
  color: #2c3e50;
  margin-top: 5px;
  margin-bottom: 5px;
  margin-left: 5px;
  margin-right: 5px;
  display: flex;
  flex-direction: column;
  height: 100%;
}


.content {
  flex: 1 0 auto;
  /* Prevent Chrome, Opera, and Safari from letting these items shrink to smaller than their content's default minimum size. */
}

.footer {
  flex-shrink: 0;
  /* Prevent Chrome, Opera, and Safari from letting these items shrink to smaller than their content's default minimum size. */
  padding: 20px 0px;
}
</style>