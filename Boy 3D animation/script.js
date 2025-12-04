gsap.to("#page1",{
    scrollTrigger:{
        trigger:`#page1`,
        start:`top top`,
        end:`bottom top`,
        markers:`true`,
        pin:`true`,
        scroller:`#main`
    }
})