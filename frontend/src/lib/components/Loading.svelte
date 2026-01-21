<div class="container">
  <span class="loader"></span>
</div>

<style>
.container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 360px;

  /* from https://cssloaders.github.io/ (MIT license) */
  .loader {
    width: 48px;
    height: 48px;
    border: 5px solid #000;
    border-bottom-color: transparent !important;
    border-radius: 50%;
    display: inline-block;
    box-sizing: border-box;
    animation: rotation 1s linear infinite;
  }
}

@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
