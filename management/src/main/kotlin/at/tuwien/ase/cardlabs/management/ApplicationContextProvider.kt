package at.tuwien.ase.cardlabs.management

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class ApplicationContextProvider : ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        Companion.applicationContext = applicationContext
    }

    companion object {
        private lateinit var applicationContext: ApplicationContext

        fun <T> getBean(beanClass: Class<T>): T = applicationContext.getBean(beanClass)
    }
}
