package org.kamired.client.manager.managers

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.util.math.BlockPos
import org.kamired.client.manager.Manager
import org.kamired.client.module.AbstractModule
import org.kamired.client.module.Category
import org.kamired.client.module.ModuleManager
import org.kamired.client.util.MotionTracker

object CombatManager : Manager {
    private val combatModules: List<AbstractModule>

    var target: EntityLivingBase? = null
        set(value) {
            motionTracker.target = value
            field = value
        }
    var placeMap = emptyMap<BlockPos, CrystalDamage>()
    var crystalMap = emptyMap<EntityEnderCrystal, CrystalDamage>()
    val motionTracker = MotionTracker(null)

    fun isActiveAndTopPriority(module: AbstractModule) = module.isActive() && isOnTopPriority(module)

    fun isOnTopPriority(module: AbstractModule): Boolean {
        return getTopPriority() <= module.modulePriority
    }

    fun getTopPriority(): Int {
        return getTopModule()?.modulePriority ?: -1
    }

    fun getTopModule(): AbstractModule? {
        var topModule: AbstractModule? = null
        for (module in combatModules) {
            if (!module.isActive()) continue
            if (module.modulePriority < topModule?.modulePriority ?: 0) continue
            topModule = module
        }
        return topModule
    }

    class CrystalDamage(val targetDamage: Float, val selfDamage: Float, val distance: Double)

    /** Use to mark a module that should be added to [combatModules] */
    annotation class CombatModule

    init {
        val cacheList = ArrayList<AbstractModule>()
        val annotationClass = CombatModule::class.java
        for (module in ModuleManager.modules) {
            if (module.category != Category.COMBAT) continue
            if (!module.javaClass.isAnnotationPresent(annotationClass)) continue
            cacheList.add(module)
        }
        combatModules = cacheList
    }
}